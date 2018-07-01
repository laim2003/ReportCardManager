import java.io.IOException;

import java.util.*;
import java.awt.*;
import java.util.logging.Logger;

import javax.swing.JFrame;

import data.Database;
import intern.Log;

public class Main {
	String cancel = "Esc";
	String errormsg;
	static ArrayList<String> names = new ArrayList<String>();
	static ArrayList<String> students = new ArrayList<String>();
	static int currentid = 0;
	static ReportCard card;
	public static Scanner scan = new Scanner(System.in);
	static Object nachname;
	static Object vorname;
	boolean loggedin = false;
	public static void main(String[] args) throws IOException {
	    JFrame window = new JFrame();
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setSize(1000, 750);
	    window.setTitle("ReportCard Manager");
	    window.setVisible(true);
		Log l = Log.instance(Main.class,"DataBaseAccess");
		Database data = new Database();
		l.log(data.getTimeCreationDate());
		System.out.println("Willkommen!\nUm dieses Programm abzubrechen, geben sie einfach 'Esc' ein.\nBitte loggen sie sich ein.");
		System.out.printf("Nutzername:");
		String usern = scan.next();
		System.out.printf("Passwort:");
		String passw = scan.next();
		LoginRequest req1 = new LoginRequest(usern,passw);
		if(req1.getValid("username")&&req1.getValid("password")) {
			boolean loggedin = false;
			manageCards();
		}
		else {
			System.out.println("Unbekannter Nutzername oder Passwort.");
			System.out.flush();
			main(args);
		}
	}
	public static void manageCards() {
		Log l = Log.instance(Main.class, "CardManager");
		System.out.println("1 : Neue Sch�lerberischt erstellen.\n2 :  Zugriff auf Sch�ler�bersicht.\n'Esc' : Abbrechen");
		String input = scan.next();
		if(input.equals("1")) { // Alle n�tigen Daten als Eingabe von Nutzer anfordern
			System.out.println("NEU ERSTELLEN:");
			System.out.println("Vorname:");
			vorname = scan.next();
			System.out.println("Nachname:");
			nachname = scan.next();
			names.add((String) vorname+"_"+(String) nachname);
			System.out.println("Klassenstufe:");
			int studentgrade = scan.nextInt();
			System.out.println("Alter:");
			int age = scan.nextInt();
			System.out.println("Geburtsdatum:");
			String birthdayraw = scan.next();
			System.out.printf("Notengebung erstellen f�r %s,%s: \n\n",nachname,vorname);
			Map<String,Integer> grades = new HashMap<String, Integer>();
			System.out.println("Bitte geben sie die F�cher des Sch�lers Kommasepariert ein:");
			String subjectsraw = scan.next();
			String[] subjects = subjectsraw.split(",");
			String[] inp = birthdayraw.split("\\.");
			Calendar cd = new GregorianCalendar();
			Date dt = new Date();
			l.log(Integer.toString(inp.length));
			cd.set(parse(inp[2]), parse(inp[1])-1, parse(inp[0]),0,0);
			dt = cd.getTime();
			l.log(dt.toString());
			currentid++;
			students.add(currentid+":"+nachname +", "+ vorname);
			for(int i = 0;i<subjects.length;i++) {
				System.out.printf("%s:",subjects[i]);
				int grade = scan.nextInt();
				grades.put(subjects[i],grade);
			}
			System.out.printf("Sch�ler %s jetzt erstellen?(J/N)\n",vorname + " " + nachname);
			String createcard = scan.next();
			if(createcard.equals("J")) {
			card = new ReportCard(vorname.toString(),nachname.toString(),studentgrade,grades,currentid);
			System.out.println("Erfolgreich erstellt");
				manageCards();
			}
			else {
				manageCards();
			}
		}
		else if(input.equals("2")) {
			System.out.println("Alle �bersischtskarten:\n");
			if(students.size() != 0) {
			for(int i = 0;i<students.size();i++) {
				System.out.println(students.get(i));
			}
			System.out.println("\nBitte die passende Zahl eingeben, oder 'Suche' eingeben um Sch�ler nach Namen zu Suchen.\n'Esc' : Abbrechen");
			}
			else{
				System.out.println("Noch keine �bersichtskarten vorhanden. Esc eingeben, um abzubrechen und neue Karte zu erstellen.");
			}
			String inp = scan.next();
			if(inp.equals("Esc")) {
				manageCards();
			}
			else if(inp.equals("Suche")) {
				searchCard();
			}
			else {
				Map<String,Integer> gradereturn  = card.requestGradesFromCard(Integer.parseInt(inp)-1);
				int currentcardindex = Integer.parseInt(inp)-1;
				String[] currentname = names.get(currentcardindex).split("_");
				System.out.printf("�bersicht f�r %s, %s:\n",currentname[1],currentname[0]);
				for ( String key : gradereturn.keySet() ) {
				System.out.println(key+" : "+gradereturn.get(key));
				}
				System.out.println("\n'Esc' : Zur�ck\n'Del' : Karte L�schen");
				inp = scan.next();
				if(inp.equals("Esc")) {
					manageCards();
				}
				else {
					card.deleteCard(currentcardindex);
					manageCards();
				}
			}
		}
	}
	public static void searchCard() {
		System.out.printf("Nach �berischtskarte suchen(Namen eingeben):");
		String keyword = scan.next();
	}
	public static int parse(String s) {
		return Integer.parseInt(s);
	}
}