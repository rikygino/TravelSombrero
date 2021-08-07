package com.art.travelsombrero

//class DataModel(val title: String?)
class DataModel(val alpha_3: String, val city: String, val imageUrl: String, val locCode: String, val state: String){
    constructor() : this("","","","","")
}
