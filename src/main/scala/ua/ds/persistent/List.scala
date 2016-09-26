package ua.ds.persistent

import scala.collection.AbstractIterator

sealed trait List[+T] {
    self =>
    def setHead[E >: T](element: E): List[E]

    def addToHead[E >: T](element: E): List[E]

    def addToTail[E >: T](element: E): List[E]

    def isEmpty: Boolean

    def size(): Int

    def contains[E >: T](element: E): Boolean

    def head: Option[T]

    def toIterator: Iterator[T] = new AbstractIterator[T] {
        var these = self

        override def next(): T = these match {
            case List.Nil => throw new NoSuchElementException
            case List.Cons(_, elem, tail) => these = tail; elem
        }

        override def hasNext: Boolean = these.size() != 0
    }

    def concatenate[E >: T](other: List[E]): List[E] = {
        this match {
            case List.Nil => other
            case List.Cons(size, elem, tail) => List.Cons(size + other.size(), elem, tail.concatenate(other))
        }
    }
}

object List {
    def apply[T](): List[T] = Nil

    case object Nil extends List[Nothing] {

        override def setHead[E >: Nothing](element: E): List[E] = addToHead(element)

        override def addToHead[E](element: E): List[E] = Cons(1, element, Nil)

        override def addToTail[E >: Nothing](element: E): List[E] = List.Cons(1, element, List.Nil)

        override def isEmpty: Boolean = true

        override def size(): Int = 0

        override def contains[E](element: E): Boolean = false

        override def head: Option[Nothing] = None
    }

    final case class Cons[+T](size: Int, elem: T, tail: List[T]) extends List[T] {

        override def setHead[E >: T](element: E): List[E] = Cons(size, element, tail)

        override def addToHead[E >: T](element: E): List[E] = Cons(size + 1, element, this)

        override def addToTail[E >: T](element: E): List[E] = Cons(size + 1, this.elem, tail.addToTail(element))

        override def isEmpty: Boolean = false

        override def contains[E >: T](element: E): Boolean = {
            if (element == elem) true
            else tail.contains(element)
        }

        override def head: Option[T] = Some(elem)
    }
}