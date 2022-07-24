package com.example.ratnikov4bilet

//Класс для дальнейшего наследования, содержащий Id класса и название объекта класса
public open class Id {
    var Id:Int = 0
    var Name:String = ""
}

//Производители(Код производителя, название)
class Developers: Id() {}

//Виды техники(Код вида, название)
class Auto: Id() {}

//Клиенты(Код клиента, ФИО, адрес)
class Clients: Id() {
    var Adres:String = ""
}

//Категории ремонтов(Код категории, название)
class CategoryRemont: Id() {}

//Ремонты(Код ремонта, Код клиента, Код вида техники, Код категории ремонта, Название техники, дата обращения, дата исполнения)
class Remont{
    var Id:Int = 0
    var Id_Client:Int = 0
    var Id_Auto:Int = 0
    var Id_Category:Int = 0
    var Id_Auto_Name:String = ""
    var DataObrashenie:String = ""
    var DataIspolnenia:String = ""
}