package MonProjet;

public class Etudiant {
    private String nom;
    private String prenom;
    private String classe;

    public Etudiant(String nom, String prenom, String classe) {
        this.nom = nom;
        this.prenom = prenom;
        this.classe = classe;
    }

    @Override
    public String toString() {
        return this.nom + " " + this.prenom + " en " + this.classe;
    }
}
