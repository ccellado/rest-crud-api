package FuncInterface

object MonadList extends Monad[List] {
  def point[A](a: => A): List[A] = List(a)
  def map[A, B](ma: List[A], f: A => B): List[B] = ma match {
    case Nil => Nil
    case x :: xs => f(x) :: map(xs ,f)
  }
  def ap[A, B](mf: List[A => B], ma: List[A]): List[B] = ma match {
    case x :: xs => mf match {
      case y :: ys => map(ma, y) ::: ap(ys, ma)
      case Nil => Nil
    }
    case Nil => Nil
  }
  def flatMap[A, B](ma: List[A], f: A => List[B]): List[B] = ma match {
    case x :: xs => f(x) ::: flatMap(xs, f)
    case Nil => Nil
  }
}
