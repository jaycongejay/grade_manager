package ca.jay.beans;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Student implements java.io.Serializable{

	
	private static final long serialVersionUID = 7524464558435547479L;
	
	private int id;
	private String studentName;
	private String studentId;
	private double exercises;
	private double assignment1;
	private double assignment2;
	private double assignment3;
	private double midterm;
	private double finalExam;
	
	private double weightedAvg;
	private String letterGrade;
	
	public void calWeightedAvg() {
		
		double weightedAvg = (this.exercises * 10 
				 + this.assignment1 * 6
				 + this.assignment2 * 12
				 + this.assignment3 * 12
				 + this.midterm * 30
			     + this.finalExam * 30) / 100;
		
		this.weightedAvg = Double.parseDouble(String.format("%.1f", weightedAvg)) ;
	}
	
	public void calLetterGrade() {
		
		if(this.weightedAvg >= 90) {
			this.letterGrade = "A+";
		}
		else if(this.weightedAvg >= 85) {
			this.letterGrade = "A";
		}
		else if(this.weightedAvg >= 80) {
			this.letterGrade = "A-";
		}
		else if(this.weightedAvg >= 75) {
			this.letterGrade = "B+";
		}
		else if(this.weightedAvg >= 70) {
			this.letterGrade = "B";
		}
		else if(this.weightedAvg >= 65) {
			this.letterGrade = "C+";
		}
		else if(this.weightedAvg >= 60) {
			this.letterGrade = "C";
		}
		else if(this.weightedAvg >= 55) {
			this.letterGrade = "D+";
		}
		else if(this.weightedAvg >= 50) {
			this.letterGrade = "D";
		}
		else {
			this.letterGrade = "F";
		}
	}
	
	
}
