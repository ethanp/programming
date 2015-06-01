package simplified

import java.io.{File, FileReader}

import scala.collection.mutable

/**
 * Ethan Petuchowski
 * 5/23/15
 */
object ShortAsPossible {
    val CHUNK_SIZE = 4
    class MetaP2P(val localPath: File, fileName: String, totalHash: String, totalSize: Long, val avbBitSet: mutable.BitSet) extends Serializable {
        def numChunks: Int = Math.ceil(totalSize.toDouble / CHUNK_SIZE).toInt
    }

    /**
     * @param avblFiles is the thing that will be saved in & loaded from XML
     */
    class User(avblFiles: Map[String, MetaP2P], partialFiles: Map[String, MetaP2P]) {
        class UserServer {
            def rcvReq(req: Request, requester: Connection): Unit = req match {

                /** not sure yet what this is
                  * maybe its part of the protocol for sending to the tracker?
                  */
                case ListFiles ⇒ avblFiles.foreach { case (h,f) ⇒ requester send f }
                case Avblty(hash: String) ⇒
                    if (avblFiles contains hash) requester send WholeFile
                    else if (partialFiles contains hash) requester send partialFiles(hash).avbBitSet
                    else requester send NoLaTengo
                case ChunkReq(hash: String, idx: Int) ⇒
                    if (avblFiles contains hash)
                        avblFiles()
                    else if (partialFiles contains hash) requester send partialFiles(hash).avbBitSet
                    else requester send NoLaTengo
            }
        }
    }
    class Chunk(hash: String, data: String)
    class Send(meta: MetaP2P) {
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

    class Download(meta: MetaP2P, inDir: File) {
        def startDL(): Unit = {
            val queue = new mutable.Queue[Char]()
            // I'm just gonna download them sequentially for now
            for (i ← 0 until meta.numChunks) {
                senders(i % senders.length).requestChunk(i, queue)
                print(queue dequeueAll yes_all mkString "")
            }
        }
        var senders = Vector.empty[Send]
        def addSender(sender: Send): Download = {
            senders = senders :+ sender
            this
        }
    }
    val yes_all: (Char) ⇒ Boolean = b ⇒ true
    def main(args: Array[String]): Unit = {
        val sampleFile = new File("samples/sample_1.txt")
        val fakeHash = "aBcD"
        val meta = new MetaP2P(sampleFile, "sample_1.txt", fakeHash, sampleFile.length(), null/*TODO bitset*/)
        val sender1 = new Send(meta)
        val sender2 = new Send(meta)
        val receiver = new Download(meta, new File("incomingDir"))
        receiver.addSender(sender1).addSender(sender2).startDL()
    }

    sealed trait Request extends Serializable
    case object ListFiles extends Request
    case class Avblty(s: String)
    trait Connection { def send(msg: Serializable): Unit }
    case object WholeFile
    case object NoLaTengo

    case class ChunkReq(hash: String, idx: Int)

}


