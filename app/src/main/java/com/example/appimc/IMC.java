package com.example.appimc;

public class IMC
{
    public static double calcularIMC (int peso, double altura)
    {
        return peso/Math.pow(altura, 2);
    }

}
