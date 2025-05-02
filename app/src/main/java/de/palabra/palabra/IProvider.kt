package de.palabra.palabra

interface IProvider {
    fun next(): GuessData?
}