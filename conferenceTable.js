angular.module('esports').component('conferenceTable', {
    controller: CTController,
    bindings: {
        cnfs : "<"
    },
    templateUrl: "E-Sports_HTML/conferenceTable.html"
});

function CTController(){

    this.BLUE = 0;
    this.GOLD = 1;
    this.PLAYOFF = 2;
    this.STATS = 3;
    this.selected = this.BLUE;

    this.winStreak = function(streak){
        if (streak >= 0)
            return true;
        else
            return false;
    };

    this.getAbs = function (streak){
        return Math.abs(streak);
    };

    this.getLeagueNames = function(){
        return ["Blue", "Gold"]
    };

    this.getLeagueKeys = function(){
        return ["nbablue", "nbagold"]
    };

}
