angular.module('esports').component('leagueButtons', {
    controller: LBController,
    bindings: {
        lgs : "<"
    },
    templateUrl: "leagueButtons.html"
});

function LBController(){
    this.BLUE = 1;
    this.GOLD = 2;
    this.selected = 0;

    this.getLeagueNames = function(){
       return ["Blue", "Gold"]
    }

    this.getLeagueKeys = function(){
        return ["blue", "gold"]
    }


}
