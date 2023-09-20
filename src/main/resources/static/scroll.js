// 준비 함수, 약간의 시간을 두어 scroll 함수를 호출하기
function prepareScroll() {
  window.setTimeout(scrollUl, 50);
}

// scroll 함수
function scrollUl() {
  // 채팅창 form 안의 ul 요소, (ul 요소 안에 채팅 내용들이 li 요소로 입력된다.)
  const chatUl = document.getElementById("message_output_layout");
  chatUl.scrollTop = chatUl.scrollHeight; // 스크롤의 위치를 최하단으로
}