package com.example.inventorycontroll.inventoryDatabase.entities

enum class GoodUnit (val value: Int){
    PCE(796), Litr(112), Kg(166);

    companion object{
        fun getGoodUnit(value: Int): GoodUnit{
            if(value==PCE.value) return PCE
            if(value==Kg.value) return  Kg
            if(value==Litr.value) return  Litr
            return PCE
        }
    }
}