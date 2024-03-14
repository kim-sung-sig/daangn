package kr.ezen.daangn.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.ezen.daangn.dao.DaangnChatMessageDAO;
import kr.ezen.daangn.dao.DaangnChatRoomDAO;
import kr.ezen.daangn.vo.ChatMessageVO;
import kr.ezen.daangn.vo.ChatRoomVO;
import kr.ezen.daangn.vo.DaangnMemberVO;

@Service(value = "chatService")
public class ChatService {
	
    @Autowired
    private DaangnChatRoomDAO daangnChatRoomDAO;
    
    @Autowired
    private DaangnChatMessageDAO daangnChatMessageDAO;
    
    @Autowired
    private DaangnMemberService daangnMemberService;
    
    @Autowired
    private DaangnMainBoardService daangnMainBoardService;
    
    /**
     * 조회
     * 채팅방 사용가능 유저가 누구인지 리턴
     * @param chatRoomIdx
     * @return
     */
    public ChatRoomVO selectChatRoom(int chatRoomIdx){
    	ChatRoomVO chatRoom = null;
    	try {
    		chatRoom = daangnChatRoomDAO.selectChatRoomByIdx(chatRoomIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return chatRoom;
    }
    
    /**
     * 조회
     * 채팅방을 만들때 이미 이전 채팅방이 있는 경우 채팅방 번호 리턴
     * 채팅방이 없는 경우 생성해서 chatRoomIdx 리턴
     * @param chatRoomVO
     * @return
     */
    public int creatChatRoom(ChatRoomVO chatRoomVO) {
    	int result = 0;
    	try {
    		if(daangnChatRoomDAO.findChatRoom(chatRoomVO) == 0) { // 없으면 만들기!
    			chatRoomVO.setBoardUserIdx(daangnMainBoardService.selectByIdx(chatRoomVO.getBoardIdx()).getUserRef());
    			daangnChatRoomDAO.createChatRoom(chatRoomVO);    			
    			result = chatRoomVO.getRoomIdx();
    		} else { // 있으면 roomIdx넘겨주기
    			result = daangnChatRoomDAO.findChatRoom(chatRoomVO);
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    /**
     * 조회 사용자의 idx로 사용자가 속한 ChatRoom을 리턴
     * @param userIdx
     * @return
     */
    public List<ChatRoomVO> selectChatRoomByUserIdx(int userIdx){
    	List<ChatRoomVO> list = null;
    	try {
			list = daangnChatRoomDAO.selectChatRoomByUserIdx(userIdx);
			for(ChatRoomVO chatRoomVO: list) {
				chatRoomVO.setBoard(daangnMainBoardService.selectByIdx(chatRoomVO.getBoardIdx()));
				chatRoomVO.setMessageList(daangnChatMessageDAO.selectChatByChatRoomIdx(chatRoomVO.getRoomIdx()));
				
				if(userIdx == chatRoomVO.getUserIdx()) {
					chatRoomVO.setMember(daangnMemberService.selectByIdx(chatRoomVO.getBoardUserIdx()));
					ChatMessageVO ch = new ChatMessageVO();
					ch.setChatRoom(chatRoomVO.getRoomIdx());
					ch.setSender(chatRoomVO.getBoardUserIdx());
					chatRoomVO.setUnreadCount(daangnChatMessageDAO.unreadCount(ch));
				} else {
					chatRoomVO.setMember(daangnMemberService.selectByIdx(chatRoomVO.getUserIdx()));
					ChatMessageVO ch = new ChatMessageVO();
					ch.setChatRoom(chatRoomVO.getRoomIdx());
					ch.setSender(chatRoomVO.getUserIdx());
					chatRoomVO.setUnreadCount(daangnChatMessageDAO.unreadCount(ch));
				}
			}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
    
    /** 조회 userIdx에 해당하는 안읽은 메시지 갯수 리턴 */
    public int selectUnReadCountByUserIdx(int userIdx) {
    	int result = 0;
    	try {
    		List<ChatRoomVO> list = daangnChatRoomDAO.selectChatRoomByUserIdx(userIdx);
			for(ChatRoomVO chatRoomVO: list) {
				chatRoomVO.setBoard(daangnMainBoardService.selectByIdx(chatRoomVO.getBoardIdx()));
				chatRoomVO.setMessageList(daangnChatMessageDAO.selectChatByChatRoomIdx(chatRoomVO.getRoomIdx()));
				
				if(userIdx == chatRoomVO.getUserIdx()) {
					chatRoomVO.setMember(daangnMemberService.selectByIdx(chatRoomVO.getBoardUserIdx()));
					ChatMessageVO ch = new ChatMessageVO();
					ch.setChatRoom(chatRoomVO.getRoomIdx());
					ch.setSender(chatRoomVO.getBoardUserIdx());
					result += daangnChatMessageDAO.unreadCount(ch);
				} else {
					chatRoomVO.setMember(daangnMemberService.selectByIdx(chatRoomVO.getUserIdx()));
					ChatMessageVO ch = new ChatMessageVO();
					ch.setChatRoom(chatRoomVO.getRoomIdx());
					ch.setSender(chatRoomVO.getUserIdx());
					result += daangnChatMessageDAO.unreadCount(ch);
				}
			}
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    
    /**
     * ChatRoomIdx에 해당하는 Message들을 리턴하는 메서드
     * @param chatRoomIdx
     * @return List<ChatMessageVO>
     */
    public List<ChatMessageVO> selectMessageByChatRoomIdx(int chatRoomIdx){
    	List<ChatMessageVO> list = null;
    	try {
    		list = daangnChatMessageDAO.selectChatByChatRoomIdx(chatRoomIdx);
    		for(ChatMessageVO messageVO : list) {
    			DaangnMemberVO dbUserData = daangnMemberService.selectByIdx(messageVO.getSender());
    			if(dbUserData != null) {
    				messageVO.setNickName(dbUserData.getNickName());
    				if(dbUserData.getUserFile() != null) {
    					messageVO.setUserProfileName(dbUserData.getUserFile().getSaveFileName());				
    				}
    			}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return list;
    }
    
    /**
     * 저장
     * 날라온 ChatMessage를 db에 저장
     * @param chatMessageVO
     */
    public void insertMessage(ChatMessageVO chatMessageVO) {
    	try {
			daangnChatMessageDAO.insertChat(chatMessageVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 수정
     * 날라온 idx로 readed--;
     * @param idx
     */
    public void updateReadCount(int idx) {
    	try {
			daangnChatMessageDAO.updateChat(idx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /** 상대방의 채팅을 모두 읽음 처리한다. */
    public void updateReadCountAll(int chatRoomIdx, int sender) {
    	try {
    		ChatMessageVO messageVO = new ChatMessageVO();
    		messageVO.setChatRoom(chatRoomIdx);
    		messageVO.setSender(sender);
    		daangnChatMessageDAO.updateAllChat(messageVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    /** 수정 채팅방의 LastUpdateDate를 업데이트 */
    public void updateChatRoomLastUpdateDate(int roomIdx) {
    	try {
			daangnChatRoomDAO.updateLastUpdateDate(roomIdx);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    
    /** 조회 idx로 ChatMessageVO 조회 */
//    public ChatMessageVO selectMessageByIdx(int idx) {
//    	ChatMessageVO messageVO = null;
//    	try {
//    		messageVO = daangnChatMessageDAO.selectByIdx(idx);
//    		if(messageVO != null) {
//    			DaangnMemberVO dbUserData = daangnMemberService.selectByIdx(messageVO.getSender());
//    			if(dbUserData != null) {
//    				messageVO.setNickName(dbUserData.getNickName());
//    				if(dbUserData.getUserFile() != null) {
//    					messageVO.setUserProfileName(dbUserData.getUserFile().getSaveFileName());    					
//    				}
//    			}  			
//    		}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//    	return messageVO;
//    }
    
}
