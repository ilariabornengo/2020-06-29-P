/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Integer> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    
    	List<Adiacenza> lista=new ArrayList<Adiacenza>(this.model.getBest());
    	for(Adiacenza a:lista)
    	{
    		txtResult.appendText(a.toString()+"\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String minS=this.txtMinuti.getText();
    	Integer mese=this.cmbMese.getValue();
    	Integer min=0;
    	try {
    		min=Integer.parseInt(minS);
    	}catch(NumberFormatException e)
    	{
    		e.printStackTrace();
    	}
    	this.model.creaGrafo(mese, min);
    	this.txtResult.appendText("GRAFO CREATO!\n");
    	this.txtResult.appendText("numero archi: "+this.model.getNArchi()+"\n");
    	this.txtResult.appendText("numero Vertici: "+this.model.getNVertici()+"\n");
    	this.cmbM1.getItems().addAll(this.model.getMatches());
    	this.cmbM2.getItems().addAll(this.model.getMatches());
    	
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	Match m1=this.cmbM1.getValue();
    	Match m2=this.cmbM2.getValue();
    	if(m1.getMatchID().equals(m2.getMatchID()))
    	{
    		txtResult.setText("NON SI POSSONO SELEZIONARE LE STESSE PARTITE");
    		return;
    	}
    	List <Match> list=this.model.lista(m1, m2);
    	txtResult.appendText("il cammino di peso massimo è:\n");
    	for(Match m:list)
    	{
    		txtResult.appendText(m.toString()+"\n");
    	}
    	txtResult.appendText("di peso "+this.model.getPesoCamminoMax());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Integer> mesi=new ArrayList<Integer>();
    	for(int i=1;i<=12;i++)
    	{
    		mesi.add(i);
    	}
    	this.cmbMese.getItems().addAll(mesi);
    }
    
    
}
