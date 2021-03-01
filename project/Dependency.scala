import sbt._

object Dependency {

  private object Version {
    val Cats          = "2.1.0"
  }

  val deps: Seq[ModuleID] = Seq(
    "org.typelevel"              %% "cats-core"            % Version.Cats,
    "org.typelevel"              %% "cats-effect"          % Version.Cats
  )

  def apply(): Seq[ModuleID]  =  deps
}
