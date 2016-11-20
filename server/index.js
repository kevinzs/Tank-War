var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var players = [];
var respawnPoints = [{x:100,y:100,rotation:0},
                     {x:850,y:190,rotation:90},
                     {x:800,y:825,rotation:180},
                     {x:40,y:710,rotation:270}];

server.listen(8080, function(){
    console.log("Server is now running...");
})

io.on('connection', function(socket){
    console.log("Player connected.");
    var x, y, rotation;
    switch(players.length){
        case 0:
            socket.emit('socketID', {id:socket.id, respawn:respawnPoints[0]});
            socket.broadcast.emit('newPlayer', {id:socket.id, respawn:respawnPoints[0]});
            x = respawnPoints[0].x;
            y = respawnPoints[0].y;
            rotation = respawnPoints[0].rotation;
            break;
        case 1:
            socket.emit('socketID', {id:socket.id, respawn:respawnPoints[1]});
            socket.broadcast.emit('newPlayer', {id:socket.id, respawn:respawnPoints[1]});
            x = respawnPoints[1].x;
            y = respawnPoints[1].y;
            rotation = respawnPoints[1].rotation;
            break;
        case 2:
            socket.emit('socketID', {id:socket.id, respawn:respawnPoints[2]});
            socket.broadcast.emit('newPlayer', {id:socket.id, respawn:respawnPoints[2]});
            x = respawnPoints[2].x;
            y = respawnPoints[2].y;
            rotation = respawnPoints[2].rotation;
            break;
        case 3:
            socket.emit('socketID', {id:socket.id, respawn:respawnPoints[3]});
            socket.broadcast.emit('newPlayer', {id:socket.id, respawn:respawnPoints[3]});
            x = respawnPoints[3].x;
            y = respawnPoints[3].y;
            rotation = respawnPoints[3].rotation;
            break;
        default:
            console.log('Servidor lleno.');
            socket.close();
            break;
    }
    socket.emit('getPlayers', players);
    socket.on('playerMoved', function(data){
        data.id = socket.id;
        socket.broadcast.emit('playerMoved', data);
        for(var i=0; i<players.length; i++){
            if(players[i].id == data.id){
                players[i].x = data.x;
                players[i].y = data.y;
                players[i].rotation = data.rotation;
            }
        }
    });
    socket.on('playerShooting', function(data){
        data.id = socket.id;
        socket.broadcast.emit('playerShooting', data);
    });
    socket.on('disconnect', function(){
        console.log("Player disconnected.");
        socket.broadcast.emit('playerDisconnected', {id: socket.id});
        for(var i=0; i<players.length; i++){
            if(players[i].id == socket.id){
                players.splice(i,1);
            }
        }
    });
    players.push(new player(socket.id, x, y, rotation));
});

function player(id, x, y, rotation){
    this.id = id;
    this.x = x;
    this.y = y;
    this.rotation = rotation;
}