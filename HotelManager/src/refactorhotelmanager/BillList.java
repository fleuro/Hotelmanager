package refactorhotelmanager;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class BillList {

	private ArrayList<Bill> billList = new ArrayList<Bill>();
	private int billCounter = 0;

	public void addBill(String category, String description, double costs, Date aDate, int amount) {
		// 0 = default bill; 1 = roomServiceBill
		billList.add(new Bill(category, description, costs, amount, aDate ));
	}

	public boolean removeBill( int number ){
		for( Bill bill: billList ){
			if( bill.getNumber() ==  number ){
				billList.remove(billList.indexOf(bill));
				return true;
			}
		}
		return false;
	}
	
	public void printAllBills() {
		System.out.printf("Omschrijving %20s Datum %20s Aantal %20s Prijs %n", " ", " ", " ");
		double costs = 0;
		for (Bill bill : billList) {
			costs += bill.getCosts();
			bill.print();
		}
		System.out.println();
		System.out.printf("Totale kosten: "+"�"+" %.2f%n", costs);
	}

	public void modifyBill(int number, String category, String description,
			double costs, Date aDate) {
		for (Bill bill : billList) {
			if (bill.getNumber() == number) {
				billList.remove(billList.indexOf(bill));
				billList.add(new Bill(category, description, costs, bill
						.getNumber(), aDate ));
			}
		}
	}

	public boolean isValidFactuurNumber(int number) {
		for (Bill bill : billList) {
			if (bill.getNumber() == number) {
				return true;
			}
		}
		return false;
	}

}