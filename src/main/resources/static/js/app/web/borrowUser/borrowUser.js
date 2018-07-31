$(function() {
	 addressInit('cmbProvince', 'cmbCity', 'cmbArea');
    var settings = {
        url: ctx + "borrowUser/list",
        pageSize: 10,
        queryParams: function(params) {
            return {
                pageSize: params.limit,
                pageNum: params.offset / params.limit + 1,
                userName: $(".borrowUser-table-form").find("input[name='userName']").val().trim(),
                phone: $(".borrowUser-table-form").find("input[name='phone']").val().trim()
            };
        },
        columns: [{
            checkbox: true
        }, {
            field: 'userName',
            title: '姓名'
        }, {
            field: 'phone',
            title: '手机号'
        }, {
            field: 'idNum',
            title: '身份证号'
        }, {
            field: 'province',
            title: '省'
        }, {
            field: 'city',
            title: '市'
        }, {
            field: 'county',
            title: '区/县'
        }, {
            field: 'address',
            title: '详细地址'
        }]
    }
    $MB.initTable('borrowUserTable', settings);
});

function search() {
    $MB.refreshTable('borrowUserTable');
}

function refresh() {
    $(".borrowUser-table-form")[0].reset();
    $MB.refreshTable('borrowUserTable');
}

function deleteBorrowUsers() {
    var selected = $("#borrowUserTable").bootstrapTable('getSelections');
    var selected_length = selected.length;
    if (!selected_length) {
        $MB.n_warning('请勾选需要删除的借款人！');
        return;
    }
    var ids = "";
    for (var i = 0; i < selected_length; i++) {
        ids += selected[i].borrowUserId;
        if (i != (selected_length - 1)) ids += ",";
    }

    $MB.confirm({
        text: "删除选中借款人，确定删除？",
        confirmButtonText: "确定删除"
    }, function() {
        $.post(ctx + 'borrowUser/delete', { "ids": ids }, function(r) {
            if (r.code == 0) {
                $MB.n_success(r.msg);
                refresh();
            } else {
                $MB.n_danger(r.msg);
            }
        });
    });
}

function exportBorrowUserExcel(){
	$.post(ctx+"borrowUser/excel",$(".borrowUser-table-form").serialize(),function(r){
		if (r.code == 0) {
			window.location.href = "common/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}

function exportBorrowUserCsv(){
	$.post(ctx+"borrowUser/csv",$(".borrowUser-table-form").serialize(),function(r){
		if (r.code == 0) {
			window.location.href = "common/download?fileName=" + r.msg + "&delete=" + true;
		} else {
			$MB.n_warning(r.msg);
		}
	});
}