package com.example.ratnikov4bilet

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val firstFragment = FirstFragment()
    private val secondFragment = SecondFragment()
    private val thirdFragment = ThirdFragment()
    private val fourFragment = FourFragment()
    private val fiveFragment = FiveFragment()

    private val db = DataBaseHandler(this)

    private var currentFragment:Fragment = firstFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bot = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bot.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_first -> replaceFragment(firstFragment, "Производители")
                R.id.ic_second -> replaceFragment(secondFragment, "Виды техники")
                R.id.ic_third -> replaceFragment(thirdFragment, "Клиенты")
                R.id.ic_four -> replaceFragment(fourFragment, "Категории ремонтов")
                R.id.ic_five -> replaceFragment(fiveFragment, "Ремонты")
            }
            true
        }
        replaceFragment(firstFragment, "Производители")

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            when (currentFragment) {
                firstFragment -> saveDataFirst()
                secondFragment -> saveDataSecond()
                thirdFragment -> saveDataThird()
                fourFragment -> saveDataFour()
                fiveFragment -> saveDataFive()
            }
            true
        }
        R.id.action_add -> {
            when (currentFragment) {
                firstFragment -> addDataFirst(0, "")
                secondFragment -> addDataSecond(0, "" )
                thirdFragment -> addDataThird(0,"","")
                fourFragment -> addDataFour(0,"")
                fiveFragment -> addDataFive(0,0,0,0,"","","")
            }
            true
        }
        R.id.action_output1 -> {
            createAlertMessage()
            true
        }
        R.id.action_output2 -> {
            createSecondAlertMessage()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun createAlertMessage(){
        val builder:AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }
        builder.setMessage(secondZadacha(db.readFour(),db.readFive()))?.setTitle("Сравнительная таблица количества сложных ремонтов ")
        builder.create()
        builder.show()
    }

    private fun createSecondAlertMessage(){
        val builder:AlertDialog.Builder = this.let {
            AlertDialog.Builder(it)
        }

        val remonts = db.readFive()
        val gotovie = this.firstZadacha(remonts)

        //(Код ремонта, Код клиента, Код вида техники, Код категории ремонта, Название техники, дата обращения, дата исполнения).
        var textt = ""
        gotovie.forEach()
        {
            textt += "${it.Id}, ${it.Id_Client}, ${it.Id_Auto}, ${it.Id_Category}, ${it.Id_Auto_Name}, ${it.DataObrashenie}, ${it.DataIspolnenia} \n"
        }
        builder.setMessage(textt)?.setTitle("Невыполненые ремонты")
        builder.create()
        builder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar,menu)
        return true
    }

    public fun Slojnaya(categoryRemont: MutableList<CategoryRemont>): Int {
        categoryRemont.forEach(){
            if (it.Name == "Сложная"){
                return it.Id
            }
        }
        return -1
    }

    //построить сравнительную таблицу количества сложных ремонтов по разным производителям
    @RequiresApi(Build.VERSION_CODES.N)
    public fun secondZadacha(categoryRemont: MutableList<CategoryRemont>, remont: MutableList<Remont>): String {

        var proizvoditely = mutableListOf<Remont>()

        remont.forEach(){
            if (it.Id_Category == Slojnaya(db.readFour())){
                proizvoditely.add(it)
            }
        }
        var proizvoditelyList = mutableMapOf<Int,Int>()
        var all_values = 0

        proizvoditely.forEach(){
            proizvoditelyList.set(it.Id_Auto, if(proizvoditelyList.getOrDefault(it.Id_Auto,-99) == -99) 1 else proizvoditelyList.getValue(it.Id_Auto) + 1)
        }

        proizvoditelyList.forEach(){
            all_values += it.value
        }

        var StringVivod = ""
        proizvoditelyList.forEach(){
            var NameProizvod = ""
            var KodProizvod = it.key
            db.readFirst().forEach(){
                if (it.Id == KodProizvod)
                    NameProizvod = it.Name
            }
            StringVivod += "${(it.value.toDouble() / all_values.toDouble())*100}% ${NameProizvod} производитель, ${it.value} ремонта(ов)\n"
        }

        return StringVivod

    }

    //Добавление строки в первую таблицу
    public fun addDataFirst(col1: Int, col2: String){
        try {
            val idCol = TextView(this)
            val nameCol = EditText(this)
            idCol.textSize = 14f
            idCol.setPadding(10)
            idCol.gravity = Gravity.CENTER_HORIZONTAL
            idCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            nameCol.textSize = 14f
            nameCol.setPadding(10)
            nameCol.gravity = Gravity.CENTER_HORIZONTAL
            nameCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            idCol.text = col1.toString()
            nameCol.setText(col2)

            val tableLayout = findViewById<TableLayout>(R.id.firstTableLayout)
            val dataRow = TableRow(this)

            dataRow.setOnLongClickListener {
                tableLayout.removeView(it)
                true
            }
            dataRow.addView(idCol)
            dataRow.addView(nameCol)
            tableLayout.addView(dataRow)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //Добавление строки во вторую таблицу
    public fun addDataSecond(col1: Int, col2: String){
        try {
            val idCol = TextView(this)
            val nameCol = EditText(this)
            idCol.textSize = 14f
            idCol.setPadding(10)
            idCol.gravity = Gravity.CENTER_HORIZONTAL
            idCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            nameCol.textSize = 14f
            nameCol.setPadding(10)
            nameCol.gravity = Gravity.CENTER_HORIZONTAL
            nameCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            idCol.text = col1.toString()
            nameCol.setText(col2)

            val tableLayout = findViewById<TableLayout>(R.id.secondTableLayout)
            val dataRow = TableRow(this)

            dataRow.setOnLongClickListener {
                tableLayout.removeView(it)
                true
            }
            dataRow.addView(idCol)
            dataRow.addView(nameCol)
            tableLayout.addView(dataRow)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //Добавление строки в третью таблицу
    public fun addDataThird(col1: Int, col2: String, col3:String){
        try {
            val idCol = TextView(this)
            val nameCol = EditText(this)
            val Adress = EditText(this)
            idCol.textSize = 14f
            idCol.setPadding(10)
            idCol.gravity = Gravity.CENTER_HORIZONTAL
            idCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            nameCol.textSize = 14f
            nameCol.setPadding(10)
            nameCol.gravity = Gravity.CENTER_HORIZONTAL
            nameCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            Adress.textSize = 14f
            Adress.setPadding(10)
            Adress.gravity = Gravity.CENTER_HORIZONTAL
            Adress.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            idCol.text = col1.toString()
            nameCol.setText(col2)
            Adress.setText(col3)

            val tableLayout = findViewById<TableLayout>(R.id.thirdTableLayout)
            val dataRow = TableRow(this)

            dataRow.setOnLongClickListener {
                tableLayout.removeView(it)
                true
            }
            dataRow.addView(idCol)
            dataRow.addView(nameCol)
            dataRow.addView(Adress)
            tableLayout.addView(dataRow)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //Добавление строки в четвёртую таблицу
    public fun addDataFour(col1: Int, col2: String){
        try {
            val idCol = TextView(this)
            val nameCol = EditText(this)
            idCol.textSize = 14f
            idCol.setPadding(10)
            idCol.gravity = Gravity.CENTER_HORIZONTAL
            idCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            nameCol.textSize = 14f
            nameCol.setPadding(10)
            nameCol.gravity = Gravity.CENTER_HORIZONTAL
            nameCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            idCol.text = col1.toString()
            nameCol.setText(col2)

            val tableLayout = findViewById<TableLayout>(R.id.fourTableLayout)
            val dataRow = TableRow(this)

            dataRow.setOnLongClickListener {
                tableLayout.removeView(it)
                true
            }
            dataRow.addView(idCol)
            dataRow.addView(nameCol)
            tableLayout.addView(dataRow)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //Добавление строки в пятую таблицу
    public fun addDataFive(col1: Int, col2: Int, col3:Int, col4:Int, col5:String, col6:String, col7:String){
        try {
            val idCol = TextView(this)
            val KodKlienta = EditText(this)
            val KodVidaTechniki = EditText(this)
            val KodCategory = EditText(this)
            val NameTechniki = EditText(this)
            val DataObrashenia = EditText(this)
            val DataIspolnenia = EditText(this)
            idCol.textSize = 14f
            idCol.setPadding(10)
            idCol.gravity = Gravity.CENTER_HORIZONTAL
            idCol.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            KodKlienta.textSize = 14f
            KodKlienta.setPadding(10)
            KodKlienta.gravity = Gravity.CENTER_HORIZONTAL
            KodKlienta.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            KodVidaTechniki.textSize = 14f
            KodVidaTechniki.setPadding(10)
            KodVidaTechniki.gravity = Gravity.CENTER_HORIZONTAL
            KodVidaTechniki.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            KodCategory.textSize = 14f
            KodCategory.setPadding(10)
            KodCategory.gravity = Gravity.CENTER_HORIZONTAL
            KodCategory.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            NameTechniki.textSize = 14f
            NameTechniki.setPadding(10)
            NameTechniki.gravity = Gravity.CENTER_HORIZONTAL
            NameTechniki.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            DataObrashenia.textSize = 14f
            DataObrashenia.setPadding(10)
            DataObrashenia.gravity = Gravity.CENTER_HORIZONTAL
            DataObrashenia.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            DataIspolnenia.textSize = 14f
            DataIspolnenia.setPadding(10)
            DataIspolnenia.gravity = Gravity.CENTER_HORIZONTAL
            DataIspolnenia.layoutParams = TableRow.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT, 4f)

            idCol.text = col1.toString()
            KodKlienta.setText(col2.toString())
            KodVidaTechniki.setText(col3.toString())
            KodCategory.setText(col4.toString())
            NameTechniki.setText(col5)
            DataObrashenia.setText(col6)
            DataIspolnenia.setText(col7)

            val tableLayout = findViewById<TableLayout>(R.id.fiveTableLayout)
            val dataRow = TableRow(this)

            dataRow.setOnLongClickListener {
                tableLayout.removeView(it)
                true
            }
            dataRow.addView(idCol)
            dataRow.addView(KodKlienta)
            dataRow.addView(KodVidaTechniki)
            dataRow.addView(KodCategory)
            dataRow.addView(NameTechniki)
            dataRow.addView(DataObrashenia)
            dataRow.addView(DataIspolnenia)
            tableLayout.addView(dataRow)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //Указание невыполненных ремонтов
    public fun firstZadacha(remont: MutableList<Remont>): MutableList<Remont> {
        var NotDone = mutableListOf<Remont>()
        remont.forEach(){
            if(it.DataIspolnenia == ""){
                NotDone.add(it)
            }
        }
        return NotDone
    }

    // Сохранение данных с первой таблицы
    private fun saveDataFirst(){
        try {
            val tableLayout = findViewById<TableLayout>(R.id.firstTableLayout)
            var dataList = mutableListOf<Developers>()
            for (num in 1 until tableLayout.childCount){
                val tableRow : TableRow = tableLayout.getChildAt(num) as TableRow
                val textView = tableRow.getChildAt(0) as TextView
                val editText = tableRow.getChildAt(1) as EditText

                val data = Developers()
                data.Id = textView.text.toString().toInt()
                data.Name = editText.text.toString()
                dataList.add(data)
            }
            db.firstUpdate(dataList)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // Сохранение данных со второй таблицы
    private fun saveDataSecond(){
        try {
            val tableLayout = findViewById<TableLayout>(R.id.secondTableLayout)
            var dataList = mutableListOf<Auto>()
            for (num in 1 until tableLayout.childCount){
                val tableRow : TableRow = tableLayout.getChildAt(num) as TableRow
                val textView = tableRow.getChildAt(0) as TextView
                val editText = tableRow.getChildAt(1) as EditText

                val data = Auto()
                data.Id = textView.text.toString().toInt()
                data.Name = editText.text.toString()
                dataList.add(data)
            }
            db.secondUpdate(dataList)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //Сохранение данных с третьей таблицы
    private fun saveDataThird(){
        try {
            val tableLayout = findViewById<TableLayout>(R.id.thirdTableLayout)
            var dataList = mutableListOf<Clients>()
            for (num in 1 until tableLayout.childCount){
                val tableRow : TableRow = tableLayout.getChildAt(num) as TableRow
                val textView = tableRow.getChildAt(0) as TextView
                val DataName = tableRow.getChildAt(1) as EditText
                val DataAdress = tableRow.getChildAt(2) as EditText

                val data = Clients()
                data.Id = textView.text.toString().toInt()
                data.Name = DataName.text.toString()
                data.Adres = DataAdress.text.toString()
                dataList.add(data)
            }
            db.thirdUpdate(dataList)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // Сохранение данных с четвёртой таблицы
        private fun saveDataFour(){
        try {
            val tableLayout = findViewById<TableLayout>(R.id.fourTableLayout)
            var dataList = mutableListOf<CategoryRemont>()
            for (num in 1 until tableLayout.childCount){
                val tableRow : TableRow = tableLayout.getChildAt(num) as TableRow
                val textView = tableRow.getChildAt(0) as TextView
                val editText = tableRow.getChildAt(1) as EditText

                val data = CategoryRemont()
                data.Id = textView.text.toString().toInt()
                data.Name = editText.text.toString()
                dataList.add(data)
            }
            db.fourUpdate(dataList)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //Сохранение данных с пятой таблицы
    private fun saveDataFive(){
        try {
            val tableLayout = findViewById<TableLayout>(R.id.fiveTableLayout)
            var dataList = mutableListOf<Remont>()
            for (num in 1 until tableLayout.childCount){
                val tableRow : TableRow = tableLayout.getChildAt(num) as TableRow
                val DataId = tableRow.getChildAt(0) as TextView
                val DataIdClient = tableRow.getChildAt(1) as EditText
                val DataIdAuto = tableRow.getChildAt(2) as EditText
                val DataIdCategory = tableRow.getChildAt(3) as EditText
                val DataIdAutoName = tableRow.getChildAt(4) as EditText
                val DataObr = tableRow.getChildAt(5) as EditText
                val DataIsp = tableRow.getChildAt(6) as EditText

                val data = Remont()
                data.Id = DataId.text.toString().toInt()
                data.Id_Client = DataIdClient.text.toString().toInt()
                data.Id_Auto = DataIdAuto.text.toString().toInt()
                data.Id_Category = DataIdCategory.text.toString().toInt()
                data.Id_Auto_Name = DataIdAutoName.text.toString()
                data.DataObrashenie = DataObr.text.toString()
                data.DataIspolnenia = DataIsp.text.toString()

                dataList.add(data)
            }
            db.fiveUpdate(dataList)
        }
        catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment, title:String){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.commit()
        currentFragment = fragment
        supportActionBar?.title = title
    }
}