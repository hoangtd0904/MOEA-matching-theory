package custom;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadInput {

	private XSSFWorkbook wb;
	private XSSFSheet sheet;
	private int startRow;
	private List<Employee> employees;
	private List<Team> teams;

	// constructor
	public ReadInput(String path, int startRow) throws IOException {
		// get file
		FileInputStream fis = new FileInputStream(path);
		// workbook
		wb = new XSSFWorkbook(fis);
		sheet = wb.getSheetAt(0);

		this.startRow = startRow;
	
		this.employees = new ArrayList<>();
		this.teams = new ArrayList<>();
	}

	// load data
	public void load() {
		int id, 
		currentRow = startRow + 1;

		//
		while (sheet.getRow(currentRow) != null) {

			id = getIntCell(sheet.getRow(currentRow).getCell(0));

			if (id < 50) this.teams.add(getTeam(currentRow));
			else this.employees.add(getEmployee(currentRow));
			
			// update current row
			currentRow++;
		}

	}
	
	/**
	 * @return list of team
	 **/
	public List<Team> getTeams(){
		return this.teams;
	}
	
	
	/**
	 *  @return list of employee
	 **/
	public List<Employee> getEmployees(){
		return this.employees;
	}

	/**
	 * @effect: return int[] by string[]
	 **/	
	private int[] getList(String[] raw) {

		List<Integer> intList = new ArrayList<>();

		for (String s : raw) {
			if (!s.equals(""))
				intList.add(Integer.parseInt(s));
		}

		//
		int[] newList = new int[intList.size()];
		for (int i = 0; i < intList.size(); i++) {
			newList[i] = intList.get(i);
		}

		return newList;
	}

	/**
	 * @effect: return string value of a cell
	 **/
	public String getStrCell(Cell cell) {
		return cell.getStringCellValue();
	}

	/**
	 * @effect: return int value of a cell
	 **/
	private int getIntCell(Cell cell) {
		
		cell.setCellType(Cell.CELL_TYPE_STRING);
		
		return Integer.parseInt(cell.getStringCellValue());
	}

	/**
	 * @effect: return new Team
	 **/
	private Team getTeam(int row) {
		Row r = sheet.getRow(row);

		return new Team(getIntCell(r.getCell(0)), getIntCell(r.getCell(1)), getIntCell(r.getCell(2)),
				getIntCell(r.getCell(3)), getIntCell(r.getCell(4)), getIntCell(r.getCell(5)), getIntCell(r.getCell(6)),
				getList(getStrCell(r.getCell(7)).split(",")));

	}

	/**
	 * @effect: return new Employee
	 **/
	private Employee getEmployee(int row) {
		Row r = sheet.getRow(row);

		return new Employee(getIntCell(r.getCell(0)), getIntCell(r.getCell(1)), getIntCell(r.getCell(2)),
				getIntCell(r.getCell(3)), getIntCell(r.getCell(4)), getIntCell(r.getCell(5)),
				getList(getStrCell(r.getCell(7)).split(",")));
	}

	// test
	public static void main(String[] args) throws IOException {
		String path = "examples/custom/data.xlsx";
		ReadInput readExcel = new ReadInput(path, 15);

		readExcel.load();
		
		System.out.println(readExcel.getEmployees().size());
		System.out.println(readExcel.getTeams().size());
	}

}// end class
