package ca.lambton.Wildemo.Models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.syncfusion.docio.*;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import ca.lambton.Wildemo.Models.WIL.MultipleChoice;
import ca.lambton.Wildemo.Models.WIL.Question;

public class Utilities {

	public static int[] loopNum(int num) {

		int[] numArray = new int[num];
		for (int i = 0; i < num; i++) {
			numArray[i] = i;
		}
		return numArray;
	}

	public static <T> int[] loopNum(int startNum, T[] genArray) {
		System.out.println(genArray.length);
		int[] numArray = new int[genArray.length - startNum];
		System.out.println(numArray.length);
		for (int i = 0; i < genArray.length - startNum; i++) {
			numArray[i] = i + startNum;
			System.out.println(i);
		}

		return numArray;
	}

	public static String slug(String str) {
		str = str.replace(' ', '-');
		return str;
	}

	public static String unSlug(String str) {
		str = str.replace('-', ' ');
		return str;
	}

	public static String getMd5(String input) {
		try {

			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String pwdExpiryDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 3);
		Date date = calendar.getTime();

		return formatter.format(date);
	}

	public static List<MultipleChoice> loadMCQQuestions() {
		List<MultipleChoice> mcq = null; // new ArrayList<MultipleChoice>();

		try {
			// declare a objectmapper object to deserialize the json file into java objects
			ObjectMapper mapper = new ObjectMapper();

			// create a list of contributors from the json file
			mcq = mapper.readValue(new URL("file:src/main/resources/Questions.json"),
					new TypeReference<List<MultipleChoice>>() {
					});

			// create a list of post from the json file
//			List<BlogPosts> listOfPost = mapper.readValue(new URL("file:src/main/resources/blogPosts.json"),
//					new TypeReference<List<BlogPosts>>() {});

			// create a blog instance using the list from json files
//			Blog blog = Blog.builder()
//					.contributors(listOfPeople)
//					.posts(listOfPost)
//					.build();

			// display the content of the blog object
//			System.out.println(blog.toString());
		}
		// catch exception if the files cannot be loaded properly
		catch (IOException e) {
			System.out.println("Sorry, could not load file.\n" + e.getMessage());
		}
		return mcq;

	}

	public static List<Question> loadQuestions() {
		List<MultipleChoice> mcq = loadMCQQuestions();
		List<Question> lstQuestion = new ArrayList<Question>();

		for (MultipleChoice m : mcq) {
			Question question = new Question();
			try {
				String[] options = new String[4];
				options[0] = m.getOption_A();
				options[1] = m.getOption_B();
				options[2] = m.getOption_C();
				options[3] = m.getOption_D();
				ObjectMapper objectMapper = new ObjectMapper();

				String arrayString = m.getQuestion() + "\n" + objectMapper.writeValueAsString(options);

				question.setAnswer(m.getAnswer());
				question.setMediaPath(m.getMediaPath());
				question.setMediaType(m.getMediaType());
				question.setQuestion(arrayString);
				question.setQuestionType("Multiple Choice");
			} catch (Exception e) {
				System.out.println(e);
			}
			lstQuestion.add(question);
		}

		return lstQuestion;
	}

	public static List<MultipleChoice> getMCQQuestions(List<Question> lstQ) {
		List<MultipleChoice> mcq = new ArrayList<MultipleChoice>();
		for (Question q : lstQ) {
			MultipleChoice mq = new MultipleChoice();
			try {
				mq.setAnswer(q.getAnswer());
				mq.setMediaPath(q.getMediaPath());
				mq.setMediaType(q.getMediaType());
				mq.setId(q.getQues_id().toString());

				String qText = q.getQuestion();
				mq.setQuestion(qText.split("\n")[0]);
				ObjectMapper objectMapper = new ObjectMapper();
				List<String> options = objectMapper.readValue(qText.split("\n")[1], new TypeReference<List<String>>() {
				});
				mq.setOption_A(options.get(0));
				mq.setOption_B(options.get(1));
				mq.setOption_C(options.get(2));
				mq.setOption_D(options.get(3));
				
				mcq.add(mq);
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		List<MultipleChoice> mcq2 = new ArrayList<MultipleChoice>();
		
		for (int i=0; i<5; i++) {
			int high = mcq.size();
			int result = new Random().nextInt(high);
			MultipleChoice random_mq = mcq.get(result);
			//random_mq.setId(""+ (i+1));
			mcq2.add(random_mq);
			mcq.remove(result);
		}
		return mcq2;
	}
	
	public static double quizScore(List<MultipleChoice> lstQ, List<String> lstAns) {
		int score = 0;
		for(int i=0; i<5; i++ ) {
			System.out.println(lstQ.get(i).getAnswer());
			System.out.println(lstAns.get(i));
			if (lstQ.get(i).getAnswer().equals(lstAns.get(i))) {
				score++;
			}
		}
		
		return (score /(double) 5) * 100;
	}
	
	
	public static void ConvertToPDF(String name, String grade) throws Exception{ //String docPath, String pdfPath) {
		//https://www.syncfusion.com/kb/12260/how-to-mail-merge-word-document-in-java
		// Create a WordDocument instance.
		WordDocument document = new WordDocument();
		String basePath = "src/main/resources/Certificate.docx";
		// Open the template Word document.
		document.open(basePath, FormatType.Docx);
		String[] fieldNames = { "Name", "Result" };
		String[] fieldValues = { name, grade };
		// Perform mail merge.
		document.getMailMerge().execute(fieldNames, fieldValues);
		// Save the Word document.
		document.save("src/main/resources/"+ name +"Result.docx");
		// Close the Word document.
		document.close();
		
		
		//https://stackoverflow.com/questions/43363624/converting-docx-into-pdf-in-java
		File inputWord = new File("src/main/resources/"+ name + "Result.docx");
	      File outputFile = new File("src/main/resources/Test_out.pdf");
	      try  {
	          InputStream docxInputStream = new FileInputStream(inputWord);
	          OutputStream outputStream = new FileOutputStream(outputFile);
	          IConverter converter = LocalConverter.builder().build();         
	          converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
	          outputStream.close();
	          System.out.println("success");
	      } catch (Exception e) {
	          e.printStackTrace();
	      }
    }
	
	

}
