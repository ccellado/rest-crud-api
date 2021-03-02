package FuncInterface

object MonadOption extends Monad[Option] {
    def point[A](a: => A): Option[A] = Some(a)
    def map[A, B](ma: Option[A], f: A => B): Option[B] = ma match {
        case None    => None
        case Some(x) => Some(f(x))
    }
    def ap[A, B](mf: Option[A => B], ma: Option[A]): Option[B] = (mf, ma) match {
        case (Some(f), Some(x)) => Some(f(x))
        case _                  => None
    }
    def flatMap[A, B](ma: Option[A], f: A => Option[B]): Option[B] = ma match {
        case None => None
        case Some(x) => f(x)
    }
}
