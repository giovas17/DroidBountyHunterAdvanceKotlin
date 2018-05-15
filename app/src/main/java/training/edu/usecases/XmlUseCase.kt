package training.edu.usecases

import android.content.res.Resources
import org.w3c.dom.Document
import org.w3c.dom.NodeList
import training.edu.droidbountyhunter.R
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Created by rdelgado on 5/9/2018.
 */
class XmlUseCase {

    lateinit var dom: Document
    lateinit var builder: DocumentBuilder

    private fun importarXML(resources: Resources) {
        try {
            val inputStream = resources.openRawResource(R.raw.fugitivos)
            dom = builder.parse(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getNodeItems(resources: Resources): NodeList?{
        try {
            val factory = DocumentBuilderFactory.newInstance()
            builder = factory.newDocumentBuilder()
            importarXML(resources)
            val root = dom.documentElement
            return root.getElementsByTagName("fugitivo")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}