package com.hotel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");
            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("Eixint del sistema... Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Llistar reserves per tipus");
        System.out.println("5. Obtindre una reserva");
        System.out.println("6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent. Ús de switch.
     */
    public static void gestionarOpcio(int opcio) {
       switch (opcio) {
        case 1:
            reservarHabitacio();
            break;
        case 2:
            alliberarHabitacio();
            break;
        case 3:
            consultarDisponibilitat();
            break;
        case 4:
            obtindreReservaPerTipus();
            break;
        case 5:
            obtindreReserva();
            break;
        case 6:
            // Eixir del programa (no fem res ací, s´encarrega el dowhile)
            break;
        default:
            System.out.println("Opció no vàlida. Seleccione una opció del 1 al 6.");
        }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
        /*  1. Tipus d'habitació disponible */
        String tipus = seleccionarTipusHabitacioDisponible();
            if (tipus == null) {
             System.out.println("No hi ha disponibilitat del tipus seleccionat.");
            return;
        }

        System.out.println("Has seleccionat: " + tipus);

        // 2. Serveis addicionals
        ArrayList<String> serveis = seleccionarServeis(); /*retorna un ArrayList<String> amb els serveis triats*/

        // 3. Càlcul del preu
        System.out.println("\nCalculem el total...\n");

        float preuHabitacio = preusHabitacions.get(tipus); /* Agafem el preu base de l'habitació des del HashMap */
        System.out.println("Preu habitació: " + preuHabitacio + " EUR");

        float subtotal = preuHabitacio; /*  El subtotal comença sent el preu de l'habitació */

        /*  Si l'usuari ha triat algun servei addicional */
        if (!serveis.isEmpty()) { 
        System.out.print("Serveis: ");
        /*  Recorrem la llista de serveis seleccionats */
        for (int i = 0; i < serveis.size(); i++) {
            String servei = serveis.get(i); /*  Nom del servei */
            float preuServei = preusServeis.get(servei); /* Preu del servei (del HashMap) */
            subtotal += preuServei; /* Sumem el preu del servei al subtotal */

            System.out.print(servei + " (" + preuServei + " EUR)"); /*  Mostrem el servei i el seu preu */
            /* Per a posar comes entre serveis (menys l'últim) */ 
            if (i < serveis.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    } else {
        System.out.println("Serveis no seleccionat");
    }

        System.out.println("\nSubtotal: " + subtotal + " EUR"); /*  Mostrem el subtotal sense IVA */

        float iva = subtotal * IVA; /* Calculem l'IVA (21%) */
        System.out.println("IVA (21%): " + iva + " EUR");

        float total = subtotal + iva; /* Total final amb IVA */
        System.out.println("TOTAL: " + total + " EUR");

        /*  4. Generar codi únic de tres xifres que no estiga repetit */
        int codi = generarCodiReserva();

        /* 5. Guardar reserva */
        /*  Crea un ArrayList amb la informació de la reserva */
        ArrayList<String> infoReserva = new ArrayList<>();
        infoReserva.add(tipus); /*  Posició 0 → tipus d'habitació */
        infoReserva.add(String.valueOf(total));/* Posicions següents → serveis */
        for (String servei : serveis) {
        infoReserva.add(servei);
        }

        reserves.put(codi, infoReserva); /* Afegim la reserva al HashMap (codi → informació) */

        /* 6. Actualitzar disponibilitat */
        int disponibles = disponibilitatHabitacions.get(tipus);
        disponibilitatHabitacions.put(tipus, disponibles - 1);

        /* 7. Missatge final */
        System.out.println("\nReserva creada amb èxit!");
        System.out.println("Codi de reserva: " + codi);
    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus (no preu).
     */
    public static String seleccionarTipusHabitacio() {
        int op;
        do {
            System.out.println("\nPer favor, tria el tipus d´habitació (1-3):");
            System.out.println("1. " + TIPUS_ESTANDARD);
            System.out.println("2. " + TIPUS_SUITE);
            System.out.println("3. " + TIPUS_DELUXE);
            op = llegirEnter("Tria el tipus (1-3): ");
        } while (op < 1 || op > 3);

        switch (op) {
            case 1:
                return TIPUS_ESTANDARD;
            case 2:
                return TIPUS_SUITE;
            case 3:
                return TIPUS_DELUXE;
            default:
            return null; /*mai entrarà */
        }
    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        System.out.println("\nTipus d´habitació disponibles:");

        /* Crida al metode implementat de mostrarInfoTipus */

        mostrarInfoTipus(TIPUS_ESTANDARD);
        mostrarInfoTipus(TIPUS_SUITE);
        mostrarInfoTipus(TIPUS_DELUXE);

        String tipus = seleccionarTipusHabitacio();

        if (disponibilitatHabitacions.get(tipus) > 0) {
            return tipus;
        } else {
            System.out.println("No queden habitacions disponibles d'aquest tipus.");
            return null;
        }
    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String. Afegeix que agafe els preus amb el hashmap perquè no n´hi ha
     * metode que visualitze preus a l´usuari.
     */
    public static ArrayList<String> seleccionarServeis() {
    ArrayList<String> serveis = new ArrayList<>();
    char resposta;

    do {
        System.out.println("\nServeis addicionals (0-4):");
        System.out.println("1. " + SERVEI_ESMORZAR + " (" 
            + preusServeis.get(SERVEI_ESMORZAR) + " EUR)");
        System.out.println("2. " + SERVEI_GIMNAS + " (" 
            + preusServeis.get(SERVEI_GIMNAS) + " EUR)");
        System.out.println("3. " + SERVEI_SPA + " (" 
            + preusServeis.get(SERVEI_SPA) + " EUR)");
        System.out.println("4. " + SERVEI_PISCINA + " (" 
            + preusServeis.get(SERVEI_PISCINA) + " EUR)");


        System.out.print("Vol afegir un servei? (s/n): ");
        resposta = sc.next().toLowerCase().charAt(0);

        if (resposta == 's') {
            int opcio = llegirEnter("Seleccione servei: ");
            String servei = null;
            
            switch (opcio) {
                case 1: servei = SERVEI_ESMORZAR; break;
                case 2: servei = SERVEI_GIMNAS; break;
                case 3: servei = SERVEI_SPA; break;
                case 4: servei = SERVEI_PISCINA; break;
                default:
                    System.out.println("Servei no vàlid.");
            }

            if (servei != null) {
                if (serveis.contains(servei)) {
                    System.out.println("Aquest servei ja està seleccionat.");
                } else {
                    serveis.add(servei);
                    System.out.println("Servei afegit: " + servei);
                }
            }
        }

    } while (resposta == 's' && serveis.size() < 4);

    return serveis;
    }


    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        /*TODO:*/
        return 0;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        
    int codi;

    do {
        /*  Genera un número aleatori entre 100 i 999 (3 xifres) */
        codi = random.nextInt(900) + 100;

       
    } while (reserves.containsKey(codi));  /* Repetim mentre el codi ja existisca */ 

    // Retornem un codi que sabem que és únic
    return codi;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");
        /*Demanar codi, tornar habitació i eliminar reserva*/

        /* Demanem el codi de reserva */
        int codi = llegirEnter("Introdueix el codi de reserva: ");

        /* Comprovem si existeix la reserva */
        if (!reserves.containsKey(codi)) {
            System.out.println("No s'ha trobat cap reserva amb aquest codi.");
            return; // eixim del mètode
        }

        /* Obtenim la informació de la reserva */
        ArrayList<String> infoReserva = reserves.get(codi);

        /*  El tipus d'habitació està en la posició 0 */
        String tipus = infoReserva.get(0);

        /* Actualitzem la disponibilitat (sumem 1 habitació)*/
        int disponibles = disponibilitatHabitacions.get(tipus);
        disponibilitatHabitacions.put(tipus, disponibles + 1);

        /* Eliminem la reserva */
        reserves.remove(codi);

        /* Missatge final */
        System.out.println("Reserva cancel·lada correctament.");
    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        /*Mostrar lliures i ocupades*/
        System.out.println("\n===== DISPONIBILITAT ACTUAL =====");
        System.out.println("Tipus\t\tLliures\tOcupades"); /*  inclou tabulador per alinear columnes */

        /* Mostrem la disponibilitat de cada tipus d'habitació per mètode auxiliar calcula i ix lliures i ocupades*/
        mostrarDisponibilitatTipus(TIPUS_ESTANDARD);
        mostrarDisponibilitatTipus(TIPUS_SUITE);
        mostrarDisponibilitatTipus(TIPUS_DELUXE);
    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
         /*  TODO: Implementar recursivitat*/
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        System.out.println("\n===== CONSULTAR RESERVA =====");
        /* Mostrar dades d'una reserva concreta */

        /* Demanem el codi */
        int codi = llegirEnter("Introdueix el codi de reserva: ");

        /* Comprovem si existeix */
        if (!reserves.containsKey(codi)) { /* Si no existe reserva */
            System.out.println("No s'ha trobat cap reserva amb aquest codi.");
            return; /* acabem i tornem al menú */
        }

        /* Si existeix, mostrem totes les dades de la reserva */
        mostrarDadesReserva(codi);
    }


    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        /*  TODO: Llistar reserves per tipus*/
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {
       /*  TODO: Imprimir tota la informació d'una reserva */
    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
                System.out.print(missatge);
                valor = sc.nextInt();
                correcte = true;
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + " EUR");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
