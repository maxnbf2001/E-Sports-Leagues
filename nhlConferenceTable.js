angular.module('esports').component('nhlConferenceTable', {
    controller: NHLCTController,
    bindings: {
        cnfs : "<"
    },
    templateUrl: "E-Sports_HTML/nhlConferenceTable.html"
});

function NHLCTController(){

    this.NHLCONFERENCE = 1;
    this.PLAYOFF = 2;
    this.selected = this.NHLCONFERENCE;

    this.winStreak = function(streak){
        if (streak >= 0)
            return true;
        else
            return false;
    };

    this.getAbs = function (streak){
        return Math.abs(streak);
    };

}
