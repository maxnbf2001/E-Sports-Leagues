angular.module('esports').component('leagueButtons', {
    controller: LBController,
    bindings: {
        lgs : "<"
    },
    templateUrl: "E-Sports_HTML/leagueButtons.html"
});

function LBController(){
    this.BLUE = 0;
    this.GOLD = 1;
    this.PLAYOFF = 2;
    this.STATS = 3;
    this.selected = this.BLUE;

    this.getLeagueNames = function(){
       return ["Blue", "Gold"]
    };

    this.getLeagueKeys = function(){
        return ["fifablue", "fifagold"]
    };





}
