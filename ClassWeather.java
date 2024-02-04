package MyWeather;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class ClassWeather
 */
public class ClassWeather extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClassWeather() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String inputdata=request.getParameter("userInput");
		String apikey="0fbfbad1316f60b07d287dd2ff2a9628";
		String city=request.getParameter("city");
		String apiUrl="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apikey;
		
	try {	
		URI uri = new URI(apiUrl);
		URL url = uri.toURL();;
		HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		InputStream  inputstream=conn.getInputStream();
		InputStreamReader reader=new InputStreamReader(inputstream);
		StringBuilder responseContent=new StringBuilder();
		Scanner scan=new Scanner(reader);
		
		
		while(scan.hasNext()) {
			responseContent.append(scan.nextLine());
			
		}
		
		scan.close();
		//System.out.println(responseContent);	
		
		  Gson gson = new Gson();
          JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
          
         // System.out.println(jsonObject);
          long dateTimestamp=jsonObject.get("dt").getAsLong()*1000;
          String date = new Date(dateTimestamp).toString();

          //Temperature
          double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
          int temperatureCelsius = (int) (temperatureKelvin - 273.15);

          //Humidity
          int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();

          //Wind Speed
          double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();

          //Weather Condition
          String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
          request.setAttribute("date", date);
          request.setAttribute("city", city);
          request.setAttribute("temperature", temperatureCelsius);
          request.setAttribute("weatherCondition", weatherCondition); 
          request.setAttribute("humidity", humidity);    
          request.setAttribute("windSpeed", windSpeed);
          request.setAttribute("weatherData", responseContent.toString());
          
          conn.disconnect();
		
	}
	catch (IOException e) {
        e.printStackTrace();
    }catch (URISyntaxException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
