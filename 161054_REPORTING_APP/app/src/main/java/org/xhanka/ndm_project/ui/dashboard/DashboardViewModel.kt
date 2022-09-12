package org.xhanka.ndm_project.ui.dashboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.Query
import opennlp.tools.doccat.DoccatModel
import opennlp.tools.doccat.DocumentCategorizerME
import opennlp.tools.sentdetect.SentenceDetectorME
import opennlp.tools.sentdetect.SentenceModel
import org.xhanka.ndm_project.R
import kotlin.random.Random


class DashboardViewModel : ViewModel() {

    private val dataBase = FirebaseDatabase.getInstance()

    init {
        dataBase.setLogLevel(Logger.Level.DEBUG)
    }

    private var _randomData: MutableLiveData<ArrayList<RandomData>> = MutableLiveData()
    val randomData: LiveData<ArrayList<RandomData>> = _randomData

    private val data = dataBase.reference.child("DATA")

    fun addData() {
        val randomNum = Random.nextInt(10, 20)
        val surnames = listOf("Dlamini", "Mamba", "Sihlongonyane", "Gama", "Mnisi", "Mkhonta")
        val dataR = RandomData("Lindelwa--$randomNum", surnames.random(), "ID: $randomNum")
        data.push().setValue(dataR.toMap())
    }

    fun removeData() {
        data.removeValue()
    }

    fun getRecyclerViewAdapter (): FirebaseRecyclerAdapter<RandomData, ViewHolder> {
        val postsQuery: Query = data
        val options: FirebaseRecyclerOptions<RandomData> = FirebaseRecyclerOptions.Builder<RandomData>()
            .setQuery(postsQuery, RandomData::class.java)
            .build()

        val adapter = object : FirebaseRecyclerAdapter<RandomData, ViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout._item, parent, false)
                )
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int, data: RandomData) {
                viewHolder.bindToViewHolder(data)
            }
        }

        return adapter
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTexView = itemView.findViewById<TextView>(R.id.name)
        private val surnameTextView = itemView.findViewById<TextView>(R.id.surname)
        private val randomNumTextView = itemView.findViewById<TextView>(R.id.randomNum)

        fun bindToViewHolder(randomData: RandomData) {
            nameTexView.text = randomData.name
            surnameTextView.text = randomData.surname
            randomNumTextView.text = randomData.randomNum
        }
    }

    fun sentences(context: Context, text: String){
        val ins = context.assets.open("en-sentence.bin")
        val sentenceModel = SentenceModel(ins)
        val sentenceDetector = SentenceDetectorME(sentenceModel)
        val sentences = sentenceDetector.sentDetect(text)

        sentences.forEach {
            Log.d("TAG", it.toString() +"\n\n")
        }
    }

    fun document(context: Context, inputText: Array<String>) {
        val ins = context.assets.open("en-sentence.bin")
        val doccatModel = DoccatModel(ins)
        val myCategorizer = DocumentCategorizerME(doccatModel)
        val outcomes = myCategorizer.categorize(inputText)
        val category = myCategorizer.getBestCategory(outcomes)
    }

}

//        val users = dataBase.reference.child("USERS")
//
//        users.get().addOnCompleteListener {
//            if (it.isSuccessful) {
//
//                for (count in it.result.children) {
//                    Log.d("TAG", count.toString())
//                }
//
//                Log.d("TAG", it.result.toString())
//
//            } else {
//                Log.d("TAG", "NOT SUCCESSFUL")
//            }
//        }

//        val languages = dataBase.reference.child("Languages")
//        languages.push().child("name").setValue("Java")
//        languages.push().child("name").setValue("Kotlin")
//        languages.push().child("name").setValue("Swift")


//        languages.addValueEventListener(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                snapshot.children.forEach {
//                    Log.d("TAG", it.value.toString())
//                }
//
//                dashboardViewModel.setTex(snapshot.toString())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                dashboardViewModel.setTex(error.details.toString())
//            }
//
//        })