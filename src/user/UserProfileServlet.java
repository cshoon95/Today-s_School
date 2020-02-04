package user;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;


@WebServlet("/UserProfileServlet")
public class UserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		MultipartRequest multi = null;
		int fileMaxSize = 10 * 1024 * 1024; //파일의 최대 크기 10MB
		String savePath = request.getRealPath("/upload").replaceAll("\\\\", "/");
		try {
			// DefaultFileRenamePolicy() : 파일의 이름이 겹치거나 하는 등 각종 오류를 처리해줌
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy()); 			
		} catch (Exception e) {
			request.getSession().setAttribute("messageType", "오류 메시지");
			request.getSession().setAttribute("messageContent", "파일 크기는 10MB를 넘을 수 없습니다.");
			response.sendRedirect("profileUpdate.jsp");
			return;
		}
		String userID = multi.getParameter("userID");
		HttpSession session = request.getSession();
		if(!userID.equals((String) session.getAttribute("userID"))) { // 자신의 아이디가 아닌데 프로필을 바꾸려는 경우
			session.setAttribute("messageType", "오류 메시지");
			session.setAttribute("messageContent", "접근할 수 없습니다.");
			response.sendRedirect("index.jsp");
			return;
		}
		String fileName ="";
		File file = multi.getFile("userProfile");
		if(file != null) { //file이 존재하는 경우
			String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1); // 파일의 확장자
			if(ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
				String prev = new UserDAO().getUser(userID).getUserProfile();
				File prevFile = new File(savePath + "/" + prev);
				if(prevFile.exists()) { //만약 파일이 존재한다면
					prevFile.delete();
				}
				fileName = file.getName();
			} else {
				if(file.exists()) {
					file.delete();
				}
				session.setAttribute("messageType", "오류 메시지");
				session.setAttribute("messageContent", "이미지 파일만 업로드가 가능합니다.");
				response.sendRedirect("profileUpdate.jsp");
				return;
			}
		}
		new UserDAO().profile(userID, fileName);
		session.setAttribute("messageType", "성공 메시지");
		session.setAttribute("messageContent", "프로필 사진이 수정되었습니다.");
		response.sendRedirect("index.jsp");
		return;		
	}

}
