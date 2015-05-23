package simplified

import java.io.{File, FileReader}

import scala.collection.mutable

/**
 * Ethan Petuchowski
 * 5/23/15
 */
object ShortAsPossible {
    val CHUNK_SIZE = 4
    class MetaP2P(val localPath: File, fileName: String, totalHash: String, totalSize: Long) {
        def numChunks = Math.ceil(totalSize.toDouble / CHUNK_SIZE).toInt
    }
    class Chunk(hash: String, data: String)
    class Sender(meta: MetaP2P) {
        def requestChunk(chunkIdx: Int, queue: mutable.Queue[Char]): Unit = {
            val arr = new Array[Char](CHUNK_SIZE)
            var ret = 0
            var off = 0
            val fileReader = new FileReader(meta.localPath)
            fileReader.skip(chunkIdx*CHUNK_SIZE)
            while (ret >= 0 && off < arr.length) {
                // returns -1 iff the end of the stream has been reached
                ret = fileReader.read(arr, off, arr.length-off)
                for (i ← off until off+ret) queue += arr(i)
                off += ret
            }
        }
    }

    class Receiver(meta: MetaP2P, inDir: File) {
        def startDL(): Unit = {
            val queue = new mutable.Queue[Char]()
            // I'm just gonna download them sequentially for now
            for (i ← 0 until meta.numChunks) {
                senders(i % senders.length).requestChunk(i, queue)
                print(queue.dequeueAll(b ⇒ true).mkString(""))
            }
        }
        var senders = Vector.empty[Sender]
        def addSender(sender: Sender): Receiver = {
            senders = senders :+ sender
            this
        }
    }

    def main(args: Array[String]): Unit = {
        val sampleFile = new File("samples/sample_1.txt")
        val fakeHash = "aBcD"
        val meta = new MetaP2P(sampleFile, "sample_1.txt", fakeHash, sampleFile.length())
        val sender1 = new Sender(meta)
        val sender2 = new Sender(meta)
        val receiver = new Receiver(meta, new File("incomingDir"))
        receiver.addSender(sender1).addSender(sender2).startDL()
    }
}
