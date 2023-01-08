// Importation of necessary packages
import akka.actor._
import akka._
import scala.concurrent._
import java.nio.file._
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString
import java.nio.file.Paths
import scala.io._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.file.scaladsl.FileTailSource
import akka.stream.scaladsl.{FileIO, Flow, Sink, Source}
import java.io._
import scala.concurrent.ExecutionContext.Implicits.global
import java.lang

object StreamProject extends App {
  // Initialize the ActorSystem
  implicit val system: ActorSystem = ActorSystem("Stream_count")
  // get data of source from the file "log-generator.log"
  val file = Paths.get("/home/benidder/IdeaProjects/Stream_project/src/main/scala/log-generator.log")
  // Define the Flow_website that will generate tuples like (website, 1)
  val Flow_website = Flow[ByteString].map { line =>
    val fields = line.utf8String.split(" ").filter(_.startsWith("www")) // take the string that begins with "www"
    val website = fields(2)
    (website, 1)
  }
  while(true){
    // Get data source from the file
    val source = FileIO.fromPath(file)
    // Generate a grouby website to make its frequence
    val mapped = source.via(Flow_website).groupBy(Int.MaxValue, _._1)
      .scan(("", 0)) { case ((_, count), (website, _)) => (website, count + 1) }
      .filter { case (website, count) => count > 0 }
      .map { case (website, count) => s"($website, $count)\n" }
      .mergeSubstreams
    // Make the result in the file website_count.txt
    val finale: Future[IOResult] =
      mapped.map(s => ByteString(s)).runWith(FileIO.toPath(Paths.get("/home/benidder/IdeaProjects/Stream_project/src/main/scala/website_count.txt")))
    mapped.runForeach(println)
    Thread.sleep(50)

  }


}