$(function(){
    //限制只能输入单词
    $(document).on("keyup change",".justEnglish,.select2-input",function(){
        this.value = this.value.replace(/[^0-9a-zA-Z\n]/g, '');
    });
    //限制只能输入数字
    $(document).on("keyup change",".justNumber",function(){
        this.value = this.value.replace(/\D/g, '');
    });
    //限制不能输入特殊字符
    $(document).on("keyup change",".limitSpecialChar",function(){
        this.value = this.value.replace(/[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/g, '');
    });
})