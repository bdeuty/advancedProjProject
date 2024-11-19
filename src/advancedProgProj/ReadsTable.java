package advancedProgProj;

import java.io.*;
import java.util.*;

public class ReadsTable {
	static class DataObject {
		List<String> columnNames;
		String subjectID;
		
		public DataObject(String subjectID) {
			this.columnNames = new ArrayList<>();
			this.subjectID = subjectID;
		}
		
		public void addColumnName(String columnName) {
			columnNames.add(columnName);
		}
		
		@Override
		public String toString() {
			return subjectID + "\nDataObject{columnNames=" + columnNames + "}";
		}
		
		public String getSubjectID()
		{
			return subjectID;
		}
		public List<String> getLowestCounts()
		{
			return columnNames;
		}
	}
	
	static class ValueWithIndex
	{
		int value;
		int index;
		
		public ValueWithIndex(int value, int index)
		{
			this.value = value;
			this.index = index;
		}
	}
	
	static String[] headers;
	
	public static void main(String[] args)
	{
		String fileName = "C:\\Users\\Bryce\\Documents\\Classes\\Advanced Programming\\TableS8_T2T_KrakenUniq_BIO_Fullset.csv";
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			//read the headers
			if ((line = reader.readLine()) != null && (line = line.trim()).length() > 0)
			{
				headers = line.split(",");
			}
			//process row data
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
		
		String subjectID = columns[0].trim();
		subjectID = subjectID.substring(1, subjectID.length()-1);
		List<ValueWithIndex> valueWithIndexes = new ArrayList<>();
		
//		String header = columns[0];
//		List<Integer> values = new ArrayList<>();
//		
		for (int i = 1; i < columns.length; i++)
		{
			try
			{
				String valueStr = columns[i].trim();
				valueStr = valueStr.replaceAll("[^0-9.-]", "");
				if (!valueStr.isEmpty())
				{
					int value = Integer.parseInt(valueStr);
					if (value > 0)
					{
						valueWithIndexes.add(new ValueWithIndex(value, i));
					}
				}
				
			} catch (NumberFormatException e) {
				System.out.println("Invalid number format for values: " + columns[i]);
			}
		}
		
		if (valueWithIndexes.size() < 5) {
			System.out.println("Not enough valid data to process");
			return null;
		}
		
		Collections.sort(valueWithIndexes, Comparator.comparingInt(v -> v.value));
		List<ValueWithIndex> smallestValues = valueWithIndexes.subList(0,5);

		
		DataObject dataObject = new DataObject(subjectID);
		
		for (ValueWithIndex valueWithIndex : smallestValues)
		{
			String header = headers[valueWithIndex.index];
			dataObject.addColumnName(header.substring(1,header.length()-1));
		}
		
		return dataObject;
		
	}
}
