function updateBorrowUser() {
    var selected = $("#borrowUserTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要修改的借款人！');
        return;
    }
    if (selected_length > 1) {
        $MB.n_warning('一次只能修改一个借款人！');
        return;
    }
    var borrowUserId = selected[0].borrowUserId;
    $.post(ctx + "borrowUser/getBorrowUser", { "borrowUserId": borrowUserId }, function(r) {
        if (r.code == 0) {
            var $form = $('#borrowUser-add');
            $form.modal();
            var borrowUser = r.msg;
            console.log(borrowUser);
            $("#borrowUser-add-modal-title").html('修改借款人');
            $form.find("input[name='borrowUserId']").val(borrowUser.borrowUserId);
            $form.find("input[name='userName']").val(borrowUser.userName);
            $form.find("input[name='idNum']").val(borrowUser.idNum);
            $form.find("input[name='phone']").val(borrowUser.phone);
            $form.find("input[name='oldphone']").val(borrowUser.phone);
            $form.find("#cmbProvince option[value='" + borrowUser.province + "']").prop("selected",true);
            var cmbProvince = document.getElementById("cmbProvince");
            var item = cmbProvince.options[cmbProvince.selectedIndex].obj;

    		for(var i=0; i<item.cityList.length; i++)
    		{
    			cmbAddOption(cmbCity, item.cityList[i].name, item.cityList[i]);
    		}
    		cmbSelect(cmbCity, borrowUser.city);
    		changeCity(borrowUser.county);
    		cmbCity.onchange = changeCity;
    		
           
            //$form.find("#cmbCity option[value='" + borrowUser.city + "']").prop("selected",true);
            $form.find("#cmbArea option[value='" + borrowUser.county + "']").prop("selected",true);
            $form.find("input[name='address']").val(borrowUser.address);
            $("#borrowUser-add-button").attr("name", "update");
        } else {
            $MB.n_danger(r.msg);
        }
    });
}
function cmbSelect(cmb, str)
{
	for(var i=0; i<cmb.options.length; i++)
	{
		if(cmb.options[i].value == str)
		{
			cmb.selectedIndex = i;
			return;
		}
	}
}
function changeCity(defaultArea)
{
	cmbArea.options.length = 0;
	if(cmbCity.selectedIndex == -1)return;
	var item = cmbCity.options[cmbCity.selectedIndex].obj;
	for(var i=0; i<item.areaList.length; i++)
	{
		cmbAddOption(cmbArea, item.areaList[i], null);
	}
	cmbSelect(cmbArea, defaultArea);
}
function cmbAddOption(cmb, str, obj)
{
	var option = document.createElement("OPTION");
	cmb.options.add(option);
	option.innerText = str;
	option.value = str;
	option.obj = obj;
}