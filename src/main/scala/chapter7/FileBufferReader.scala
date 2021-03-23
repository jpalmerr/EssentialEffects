package chapter7

import cats.effect._
import java.io.RandomAccessFile


class FileBufferReader private (in: RandomAccessFile) {
  def readBuffer(offset: Long): IO[(Array[Byte], Int)] =
    IO { in.seek(offset)

      val buf = new Array[Byte](FileBufferReader.bufferSize)
      val len = in.read(buf)
      (buf, len)
    }

  // Since we want the Resource to manage the hidden state, we make the close method inaccessible from outside callers
  private def close: IO[Unit] = IO(in.close())
}
object FileBufferReader {
  val bufferSize = 4096

  // We make our Resource by creating the FileBufferReader in an IO effect, ensuring that we close the state when the Resource is released
  def makeResource(fileName: String): Resource[IO, FileBufferReader] =
    Resource.make {
      IO(new FileBufferReader(new RandomAccessFile(fileName, "r")))
    } { res =>
    res.close
    }
}
