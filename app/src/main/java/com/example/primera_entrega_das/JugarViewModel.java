package com.example.primera_entrega_das;

import androidx.lifecycle.ViewModel;

import androidx.lifecycle.ViewModel;

//clase para mantener la pregunta y controlar los giros en la clase JugarActivity
public class JugarViewModel extends ViewModel {
        private ModeloPregunta pregunta;

        public ModeloPregunta getPregunta() {
            return pregunta;
        }

        public void setPregunta(ModeloPregunta pregunta) {
            this.pregunta = pregunta;
        }

}