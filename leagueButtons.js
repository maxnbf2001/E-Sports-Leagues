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
    this.selected = this.BLUE;

    this.getLeagueNames = function(){
       return ["Blue", "Gold"]
    }

    this.getLeagueKeys = function(){
        return ["blue", "gold"]
    }

    this.$onInit = function(){
        this.fixtureArray = this.lgs[this.getLeagueKeys()[this.selected-1]].fixtures;
    }




}
