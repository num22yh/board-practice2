
function validatePostForm() {
    const categoryId = document.forms["postForm"]["categoryId"].value;
    if (categoryId === "") {
        alert("카테고리를 선택해 주세요.");
        return false;
    }

    const author = document.forms["postForm"]["author"].value;
    if (author === "") {
        alert("작성자를 입력해주세요.");
        return false;
    }

    const password = document.forms["postForm"]["password"].value;
    const confirmPassword = document.forms["postForm"]["confirmPassword"].value;
    if (password.length < 4 || password.length > 16 || !/[a-zA-Z]/.test(password) || !/[0-9]/.test(password) || !/[!@#$%^&*]/.test(password)) {
        alert("비밀번호는 영문, 숫자, 특수문자를 포함하여 4자 이상, 16자 미만이어야 합니다.");
        return false;
    }

    if (password !== confirmPassword) {
        alert("비밀번호 확인이 일치하지 않습니다.");
        return false;
    }

    const title = document.forms["postForm"]["title"].value;
    if (title.length < 4 || title.length > 100) {
        alert("제목은 4자 이상, 100자 미만이어야 합니다.");
        return false;
    }

    const content = document.forms["postForm"]["content"].value;
    if (content.length < 4 || content.length > 2000) {
        alert("내용은 4자 이상, 2000자 미만이어야 합니다.");
        return false;
    }

    return true;
}
