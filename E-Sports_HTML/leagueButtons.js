angular.module('esports').component('leagueButtons', {
    controller: WBController,
    bindings: {
        lg_list : "<"
    },
    templateUrl: "leagueButtons.html"
});

function WBController(){
    this.selected = 0;


}
