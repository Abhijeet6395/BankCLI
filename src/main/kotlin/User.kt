fun main(){
    var customer= mutableMapOf<Int,String>()
    customer[3]="Customer 1"
    customer[4]="Costumer 2"
    customer[5]="Costumer 3"
    customer[6]="Costumer 4"
    for((key,value) in customer){
        println(value)
    }
}