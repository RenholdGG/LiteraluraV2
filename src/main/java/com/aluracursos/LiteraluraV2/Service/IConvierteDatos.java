package com.aluracursos.LiteraluraV2.Service;

public interface IConvierteDatos {
    <T> T convierteDatos(String json, Class<T> clase);

}
