package com.art.travelsombrero

class DestinationDataModel(val alpha_3: String,
                           val city: String,
                           val farnesina: String,
                           val food: String,
                           val imageUrl: String,
                           val lat: String,
                           val locCode: String,
                           val lon: String,
                           val news: String,
                           val state: String,
                           val turistic: String){
    constructor() : this("","","","","", "", "", "","","","")
}
