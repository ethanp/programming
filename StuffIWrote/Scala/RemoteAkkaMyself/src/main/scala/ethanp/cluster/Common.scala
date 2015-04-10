package ethanp.cluster

import akka.actor.{RootActorPath, ActorSelection, ActorSystem}
import com.typesafe.config.ConfigFactory

/**
 * Ethan Petuchowski
 * 4/10/15
 */
object Common {
    def clusterSystem(port: String, role: String) = {
        val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
          withFallback(ConfigFactory.parseString(s"akka.cluster.roles = [$role]")).
          withFallback(ConfigFactory.load())
        ActorSystem("ClusterSystem", config)
    }
}
