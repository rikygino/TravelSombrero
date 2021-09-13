package com.art.travelsombrero

class MeteoDataModel(val temp_day: String, val temp_min: String, val temp_max: String, val wind: String, val iconUrl: String,val description: String){
    constructor() : this("","","","", "", "")
}