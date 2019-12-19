angular.module('esports').component('conferenceTable', {
    controller: CTController,
    bindings: {
        cnfs : "<"
    },
    templateUrl: "E-Sports_HTML/conferenceTable.html"
});

function CTController(){

    this.CONFERENCE = 1;
    this.PLAYOFF = 2;
    this.selected = this.CONFERENCE;

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
