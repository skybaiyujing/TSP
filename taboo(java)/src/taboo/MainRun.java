package taboo;

public class MainRun {
	static int lenth=17;
	public static void main(String[] args) {
		TabooSearch obj=new TabooSearch();
		 int[] solutionlast=obj.taboo_search();
		 for(int i=0;i<solutionlast.length;i++) {
			 System.out.print(solutionlast[i]+"-->");
		 }
		 System.out.print(solutionlast[0]);
		 int valuelast=obj.route_cost(solutionlast);
		 System.out.println('\n'+"Ωœ”≈æ‡¿ÎŒ™"+valuelast);
	}
}
