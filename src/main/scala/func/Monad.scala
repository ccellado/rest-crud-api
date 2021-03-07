package func

trait Monad[M[_]] {
  def point[A](a: => A): M[A]
  def map[A, B](ma: M[A], f: A => B): M[B]
  def ap[A, B](mf: M[A => B], ma: M[A]): M[B]
  def flatMap[A, B](ma: M[A], f: A => M[B]): M[B]
}