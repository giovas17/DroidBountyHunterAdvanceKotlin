package training.edu.fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import training.edu.droidbountyhunter.R

/**
 * Created by rdelgado on 5/4/2018.
 */
class About : Fragment() {

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Se hace referencia al Fragment generado por XML en los Layouts y
        // se instanc’a en una View...
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        // Se accede a los elementos ajustables del Fragment...
        val ratingBar = view.findViewById<View>(R.id.ratingBar) as RatingBar
        var ratingString = "0.0" // Variable para lectura del Rating guardado en el property
        try {
            if (System.getProperty("rating") != null) {
                ratingString = System.getProperty("rating")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (ratingString.isEmpty()) {
            ratingString = "0.0"
        }
        ratingBar.rating = java.lang.Float.valueOf(ratingString)
        // Listener al Raiting para la actualizacion de la property...
        ratingBar.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { _, rating, _ ->
            System.setProperty("rating", rating.toString())
            ratingBar.rating = rating
        }
        return view
    }
}