package fi.jyu.ties4520.cottage;

public class LoaderCheck {
	public static void main(String[] args) {
        new CottageRepository().getAllCottages().forEach(System.out::println);
    }
}
