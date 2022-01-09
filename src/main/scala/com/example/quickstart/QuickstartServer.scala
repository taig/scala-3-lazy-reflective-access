package com.example.quickstart

import cats.effect.{Async}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object QuickstartServer {

  def stream[F[_]: Async]: Stream[F, Nothing] = {
    Stream.resource(EmberClientBuilder.default[F].build).flatMap { client =>
      val helloWorldAlg = HelloWorld.impl[F]
      val jokeAlg = Jokes.impl[F](client)

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      val httpApp = (
        QuickstartRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
          QuickstartRoutes.jokeRoutes[F](jokeAlg)
        ).orNotFound

      // With Middlewares in place
      val finalHttpApp = Logger.httpApp(true, true)(httpApp)

      Stream.resource(
      EmberServerBuilder.default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(finalHttpApp)
        .build
    ) >> Stream.never
    }
  }.drain
}
