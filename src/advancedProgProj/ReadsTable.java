package advancedProgProj;

import java.io.*;
import java.util.*;

public class ReadsTable {
	static class DataObject {
		List<String> columnNames;
		
		public DataObject() {
			this.columnNames = new ArrayList<>();
		}
		
		public void addColumnName(String columnName) {
			columnNames.add(columnName);
		}
		
		@Override
		public String toString() {
			return "DataObject{columnNames=" + columnNames + "}";
		}
	}
	
	public static void main(String[] args)
	{
		String fileName = "C:\\Users\\Bryce\\Documents\\Classes\\Advanced Programming\\TableS8_T2T_KrakenUniq_BIO_Fullset.csv";
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				DataObject dataObject = parseRow(line);
				
				if (dataObject != null) {
					System.out.println(dataObject);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static DataObject parseRow(String line)
	{
		String[] columns = line.split(",");
		if (columns.length < 19) {
			System.out.println("Skipping row due to insufficient columns.");
			return null;
		}
		String[] headers = columns[0].split(",");
//		for (String head : headers)
//		{
//			System.out.println(head);
//		}
		List<Double> values = new ArrayList<>();
		
		for (int i = 1; i < columns.length; i++)
		{
			try
			{
				String valueStr = columns[i].trim();
				valueStr = valueStr.replaceAll("[^0-9.-]", "");
				if (!valueStr.isEmpty())
				{
					double value = Double.parseDouble(columns[i].trim());
					if (value > 0)
					{
						values.add(value);
					}
				}
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid number format for values: " + columns[i]);
			}
		}
		
		if (values.size() < 5) {
			System.out.println("Not enough valid data to process");
			return null;
		}
		
		Collections.sort(values);
		List<Double> smallestValues = values.subList(0,5);
		
		DataObject dataObject = new DataObject();
		
		for (Double value : smallestValues)
		{
			for (int i = 0; i < columns.length; i++)
			{
				try {
					double parsedValue = Double.parseDouble(columns[i].trim());
					if (parsedValue == value)
					{
						if (i < headers.length) {
							dataObject.addColumnName(headers[i]);
						}
					}
				} catch (NumberFormatException e)
				{
					System.out.println("idk bro");
				}
			}
		}
		
		return dataObject;
		
	}
}
