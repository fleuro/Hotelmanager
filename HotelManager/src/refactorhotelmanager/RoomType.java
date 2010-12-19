package refactorhotelmanager;
public enum RoomType {
	
	normalSmall(1, 1,60),
	normalMedium(2, 2, 70),
	normalLarge(3, 4, 80),
	economySmall(4, 1, 75),
	economyMedium(5, 2, 85),
	economyLarge(6, 4, 95),
	businessSmall(7, 1, 90),
	businessMedium(8, 2, 100),
	businessLarge(9, 4, 110),
	luxurySmall(10, 1, 110),
	luxuryMedium(11, 2, 120),
	luxuryLarge(12, 3, 130),
	honeymoon(13, 2, 120);

	public double dayPrice;
	public int numberOfPerson;
	public int order;

	RoomType(int order, int numberOfPerson,int dayPrice){
		this.dayPrice = dayPrice;
		this.numberOfPerson = numberOfPerson;
		this.order = order;
	}

	public double getDayPrice(){
		return dayPrice;
	}

	public int getOrder(){
		return order;
	}

	public int getnumberOfPerson(){
		return numberOfPerson;
	}
}