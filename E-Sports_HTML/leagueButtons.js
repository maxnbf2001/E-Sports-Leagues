angular.module('esports').component('leagueButtons', {
    controller: LBController,
    bindings: {
        lg_list : "<"
    },
    templateUrl: "leagueButtons.html"
});

function LBController ()
{
    this.selected = 0;

    this.$onInit = function(){
        this.lgs = this.lg_list.blue;
    }
}