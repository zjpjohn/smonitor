function getFieldsHtml(fields, obj) {
    var fhtml="";
    $.each(fields,function(i,item){
        var val=obj[item.fieldName];
        if (typeof(val) == "undefined") {
            val="";
        }
        fhtml+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input value='"+val+"' type='text' class='form-control' name='"+item.fieldName+"'/></div>";
    });
    return fhtml;
};
$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};